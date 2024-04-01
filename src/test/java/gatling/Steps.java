package gatling;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.http.HttpDsl;
import static io.gatling.javaapi.core.CoreDsl.StringBody;

public class Steps {
    public static ChainBuilder task = CoreDsl.exec(
            HttpDsl.http("createTask")
                    .post("/tasks")
                    .body(StringBody("{\"description\": \"TaskTest\", \"dueDate\": \"2024-03-24\",\"status\": \"PLANNED\", \"userId\": 1}"))
                    .asJson()
                    .check(HttpDsl.status().is(201)),
            HttpDsl.http("getAllTasks")
                    .get("/tasks?pageNo=0&pageSize=10")
                    .check(HttpDsl.status().is(200)),
            HttpDsl.http("updateTask")
                    .put("/updateTask")
                    .header("Content-Type", "application/json")
                    .header("Accept-Language", "uk")
                    .body(StringBody("{\"description\": \"TaskTest\", \"dueDate\": \"2024-03-24\",\"status\": \"WORK_IN_PROGRESS\", \"userId\": 1}"))
                    .asJson()
                    .check(HttpDsl.status().is(403)),
            HttpDsl.http("getTaskByUserId")
                    .get("/tasks/user/1?pageNo=0&pageSize=10")
                    .check(HttpDsl.status().is(200)),
            HttpDsl.http("getTaskById")
                    .get("/tasks/1")
                    .check(HttpDsl.status().is(200))
    );

}


