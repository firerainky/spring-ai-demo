package spring.ai.example.spring_ai_demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjic.sdk.utils.SignatureUtils;

public class ZjicRoleTools {

    private final static String url = "https://pre-zjic.zhejianglab.com";
    private final static String accessKey = "DJXNG5IXsCAwEAAQ";
    private final static String secretKey = "7+QmGWqwPlrnDd8x7oubn8sk+7Hfdc";

    private final static String ACCESS_KEY = "X-API-Key";
    private final static String PORTAL_ID = "X-Portalid";
    private final static String SIGNATURE_PREFIX = "Signature-";
    private final static String AUTHORIZATION = "Authorization";

    @Tool(description = "获取之江智算平台上的用户角色")
    String listRoles() {
        SimpleRequestExecutor executor = new SimpleRequestExecutor();
        String fullUrl = url + "/apis/sso/v1/role/list";
        Map<String, Object> params = new HashMap<>();
        params.put("current", 1);
        params.put("pageSize", 100);

        String signature = SignatureUtils.sign(secretKey, params);

        Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json", ACCESS_KEY, accessKey, AUTHORIZATION, signature, PORTAL_ID, "11");

        String response = executor.executePostRequest(fullUrl, headers, mapToJson(params));

        System.out.println("Response: " + response);

        return response;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 Map 对象转换为 JSON 格式的字符串。
     *
     * @param map 需要转换的 Map 对象。
     * @return 转换后的 JSON 字符串；如果转换失败，则返回 null。
     */
    public static String mapToJson(Map<String, ?> map) {
        if (map == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // 在实际应用中，这里最好使用日志框架记录错误
            e.printStackTrace();
            return null;
        }
    }
}
