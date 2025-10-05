ktor:
    application:
        modules:
            - com.ril.ApplicationKt.module
    deployment:
        port: ${PORT:8080}
        host: 0.0.0.0
