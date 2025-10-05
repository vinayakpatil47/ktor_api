package com.ril

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

// HTTP Client for Supabase API calls
val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

private val users = mutableListOf(
  User(1, "Vinayak", "vinayak@ril.com"),
  User(2, "Patil", "abc@super.com"),
  User(3, "Aman", "aman@jio.com"),
  User(4, "Viren", "viren@jio.com"),
  User(4, "Kishor", "kk@jio.com")
)

suspend fun fetchAndroidUsersFromSupabase(): List<User> {
  val response: HttpResponse = client.get("https://csdjgmwlpyuajiklymgd.supabase.co/rest/v1/User_android") {
    headers {
      append("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNzZGpnbXdscHl1YWppa2x5bWdkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTg4MDQ2OTYsImV4cCI6MjA3NDM4MDY5Nn0.6MKStgdXSNN-OIpwXRST9jf0OjaAWi3ZYhEMwZLmKoI")
      append("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNzZGpnbXdscHl1YWppa2x5bWdkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTg4MDQ2OTYsImV4cCI6MjA3NDM4MDY5Nn0.6MKStgdXSNN-OIpwXRST9jf0OjaAWi3ZYhEMwZLmKoI")
    }
  }
  return response.body()
}

suspend fun fetchIosUsersFromSupabase(): List<User> {
  val response: HttpResponse = client.get("https://csdjgmwlpyuajiklymgd.supabase.co/rest/v1/User_ios") {
    headers {
      append("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNzZGpnbXdscHl1YWppa2x5bWdkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTg4MDQ2OTYsImV4cCI6MjA3NDM4MDY5Nn0.6MKStgdXSNN-OIpwXRST9jf0OjaAWi3ZYhEMwZLmKoI")
      append("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNzZGpnbXdscHl1YWppa2x5bWdkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTg4MDQ2OTYsImV4cCI6MjA3NDM4MDY5Nn0.6MKStgdXSNN-OIpwXRST9jf0OjaAWi3ZYhEMwZLmKoI")
    }
  }
  return response.body()
}

fun Route.userRoutes() {
  route("/users") {

    // GET all users
    get {
      call.respond(users)
    }

    get("/User_android") {
      val users = fetchAndroidUsersFromSupabase()
      call.respond(users)
    }
    get("/User_ios") {
      val users = fetchIosUsersFromSupabase()
      call.respond(users)
    }

    // GET by id
    get("{id}") {
      val id = call.parameters["id"]?.toIntOrNull()
      val user = users.find { it.id == id }
      if (user != null) {
        call.respond(user)
      } else {
        call.respond(HttpStatusCode.NotFound, "User not found")
      }
    }

    // POST - create new user
    post {
      val newUser = call.receive<User>()
      users.add(newUser)
      call.respond(HttpStatusCode.Created, newUser)
    }

    // PUT - update user
    put("{id}") {
      val id = call.parameters["id"]?.toIntOrNull()
      val index = users.indexOfFirst { it.id == id }
      if (index != -1) {
        val updatedUser = call.receive<User>()
        users[index] = updatedUser
        call.respond(updatedUser)
      } else {
        call.respond(HttpStatusCode.NotFound, "User not found")
      }
    }

    // DELETE - remove user
    delete("{id}") {
      val id = call.parameters["id"]?.toIntOrNull()
      val removed = users.removeIf { it.id == id }
      if (removed) {
        call.respond(HttpStatusCode.NoContent)
      } else {
        call.respond(HttpStatusCode.NotFound, "User not found")
      }
    }
  }
}
