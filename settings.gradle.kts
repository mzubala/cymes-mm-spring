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
include("commons:commons-test")
findProject(":commons:commons-test")?.name = "commons-test"
include("commons:commons-application")
findProject(":commons:commons-application")?.name = "commons-application"
include("commons:shared-kernel")
findProject(":commons:shared-kernel")?.name = "shared-kernel"
include("commons:commons-configuration")
findProject(":commons:commons-configuration")?.name = "commons-configuration"
include("users")
include("notifications")
include("commons:commons-events")
findProject(":commons:commons-events")?.name = "commons-events"
