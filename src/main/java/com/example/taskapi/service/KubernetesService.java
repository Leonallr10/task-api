package  com.example.taskapi.service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class KubernetesService {

    private final CoreV1Api api;

    public KubernetesService() throws Exception {
        // Assumes that the application is running in an environment where it can access kubeconfig.
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        this.api = new CoreV1Api();
    }

    /**
     * Creates a pod that runs the provided command.
     * Returns the pod's output as a placeholder (in real life you might stream logs).
     */
    public String runCommandInPod(String command) throws Exception {
        String podName = "task-exec-" + UUID.randomUUID().toString().substring(0, 5);

        V1Pod pod = new V1Pod()
            .metadata(new V1ObjectMeta().name(podName))
            .spec(new V1PodSpec()
                .containers(Arrays.asList(new V1Container()
                    .name("task-container")
                    .image("busybox")
                    // Busybox: use sh to run the command; adjust arguments as needed
                    .command(Arrays.asList("sh", "-c", command))
                ))
                .restartPolicy("Never")
            );

        // Create the pod in the default namespace (or specify another namespace)
// Using empty strings for optional parameters and null for the "pretty" parameter if allowed
            api.createNamespacedPod("default", pod, null, "", "", "");

        // In a real implementation, you would wait for the pod to complete and retrieve logs.
        // For demonstration, we assume the command executes quickly.
        // For example, using:
        // String logs = api.readNamespacedPodLog(podName, "default", null, false, Integer.MAX_VALUE, null, Boolean.FALSE, Integer.MAX_VALUE, Boolean.FALSE);
        // Then delete the pod.
        // Here we return a dummy output.
        return "Command executed in pod " + podName;
    }
}
