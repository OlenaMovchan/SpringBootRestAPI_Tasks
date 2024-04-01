package gatling;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class GatlingTest extends Simulation {

    HttpProtocolBuilder httpProtocol = HttpDsl.http.baseUrl("http://springusers-env.eba-macic9ka.eu-central-1.elasticbeanstalk.com")
            .acceptLanguageHeader("uk")
            .basicAuth("2", "2");

    public GatlingTest(){
        this.setUp(
                Scenario.taskScen.injectClosed(
                        CoreDsl.constantConcurrentUsers(10).during(10),
                        CoreDsl.rampConcurrentUsers(5).to(75).during(30)
                )
//                Scenario.taskScen.injectOpen(
//                       CoreDsl.constantUsersPerSec(30).during(30)
//                )

        ).protocols(httpProtocol);
    }
}
