package com.pago.dotodo.util;

import com.pago.dotodo.security.CustomSecurityConfig;
import com.pago.dotodo.service.LayoutService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class ModelAndViewParser {

    private final LayoutService layoutService;
    private final String[] administrationRoles;

    public ModelAndViewParser(LayoutService layoutService) {
        this.layoutService = layoutService;
        this.administrationRoles = CustomSecurityConfig.getAdministrationRoles();
    }

    public Map<String, Object> build(Object... attributes) {
        // Validate that the number of arguments is even
        if (attributes.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid number of arguments. Key-value pairs are required.");
        }

        // Create the map of specific attributes
        Map<String, Object> specificAttributes = IntStream.range(0, attributes.length / 2)
                .boxed()
                .collect(HashMap::new,
                        (map, i) -> map.put((String) attributes[i * 2], attributes[i * 2 + 1]),
                        HashMap::putAll);

        // Merge specific attributes with common attributes
        Map<String, Object> combinedAttributes = new HashMap<>(addNavAttributes());
        combinedAttributes.putAll(specificAttributes);

        return combinedAttributes;
    }

    private Map<String, Object> addNavAttributes() {
        Map<String, Object> navAttributes = new HashMap<>();
        navAttributes.put("topbarNavItems", layoutService.getTopbarNavItems());
        navAttributes.put("sidebarNavItems", layoutService.getSidebarNavItems());
        navAttributes.put("bottombarNavItems", layoutService.getBottombarNavItems());
        navAttributes.put("connectNavItems", layoutService.getConnectNavItems());
        navAttributes.put("administrationRoles", administrationRoles);
        return navAttributes;
    }
}