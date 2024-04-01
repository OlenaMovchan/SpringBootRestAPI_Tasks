package gatling;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;

public class Scenario {
   public static ScenarioBuilder taskScen = CoreDsl.scenario("tasks scenario")
            .exec(Steps.task);
}
