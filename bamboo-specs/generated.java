import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.BambooOid;
import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.plan.branches.BranchCleanup;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.AllOtherPluginsConfiguration;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.ConcurrentBuilds;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.builders.trigger.RepositoryPollingTrigger;
import com.atlassian.bamboo.specs.util.BambooServer;
import com.atlassian.bamboo.specs.util.MapBuilder;

@BambooSpec
public class PlanSpec {

    public Plan plan() {
        final Plan plan = new Plan(new Project()
                .oid(new BambooOid("1klogu67oyha9"))
                .key(new BambooKey("AL"))
                .name("Allure"),
                "Generated",
                new BambooKey("PLAN"))
                .oid(new BambooOid("1klermkuh4ow2"))
                .pluginConfigurations(new ConcurrentBuilds(),
                        new AllOtherPluginsConfiguration()
                                .configuration(new MapBuilder()
                                        .put("custom", new MapBuilder()
                                                .put("allure", new MapBuilder()
                                                        .put("server", new MapBuilder()
                                                                .put("token", "123123")
                                                                .put("endpoint", ""123123)
                                                                .build())
                                                        .put("upload.enabled", "false")
                                                        .put("project.id", "123")
                                                        .put("launch", new MapBuilder()
                                                                .put("tags", "1231231")
                                                                .put("name", "${buildPlanName} - #${buildNumber}")
                                                                .build())
                                                        .put("artifact.name", "")
                                                        .put("config", new MapBuilder()
                                                                .put("failed.only", "false")
                                                                .put("executable", "allure-2.7.0")
                                                                .put("enabled", "true")
                                                                .build())
                                                        .build())
                                                .put("buildExpiryConfig.enabled", "false")
                                                .build())
                                        .build()))
                .stages(new Stage("Default Stage")
                        .jobs(new Job("Default Job",
                                new BambooKey("JOB1"))
                                .artifacts(new Artifact()
                                        .name("allure-results")
                                        .copyPattern("build/allure-results/**")
                                        .required(true))
                                .tasks(new VcsCheckoutTask()
                                                .description("Checkout Default Repository")
                                                .checkoutItems(new CheckoutItem().defaultRepository()),
                                        new ScriptTask()
                                                .description("Test")
                                                .inlineBody("./gradlew clean test"))))
                .linkedRepositories("allure-example-bamboo")

                .triggers(new RepositoryPollingTrigger())
                .planBranchManagement(new PlanBranchManagement()
                        .delete(new BranchCleanup())
                        .notificationForCommitters())
                .forceStopHungBuilds();
        return plan;
    }

    public PlanPermissions planPermission() {
        final PlanPermissions planPermission = new PlanPermissions(new PlanIdentifier("AL", "MAN"))
                .permissions(new Permissions()
                        .userPermissions("admin", PermissionType.EDIT, PermissionType.VIEW, PermissionType.ADMIN, PermissionType.CLONE, PermissionType.BUILD));
        return planPermission;
    }

    public static void main(String... argv) {
        //By default credentials are read from the '.credentials' file.
        BambooServer bambooServer = new BambooServer("http://172.21.0.2:8085");
        final PlanSpec planSpec = new PlanSpec();

        final Plan plan = planSpec.plan();
        bambooServer.publish(plan);

        final PlanPermissions planPermission = planSpec.planPermission();
        bambooServer.publish(planPermission);
    }
}