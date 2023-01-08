rootProject.name = "cymes-mm-spring"

include("application")
include("commons")
include("movies")
include("cinemas")
include("show-scheduler")
include("reservations")
include("notifications")
include("tickets-generation")
include("user")
include("commons:commons-rest")
findProject(":commons:commons-rest")?.name = "commons-rest"
