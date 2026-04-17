package org.example.order_inventory_system.controller;
 
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
 
@Controller
public class HomeController {
 
    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "landing"; // public landing page
        }
 
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
 
        if (isAdmin) {
            return "home"; // admin dashboard
        } else {
            return "redirect:/customer/dashboard";
        }
    }
 
    @GetMapping("/member/{slug}")
    public String memberDetails(@PathVariable String slug, Model model) {
        String name;
        List<Map<String, Object>> services = new ArrayList<>();
        
        switch (slug) {
            case "atharva-dhemare":
                name = "Atharva Dhemare";
                services.add(createServiceData("Customer", "/customers"));
                services.add(createServiceData("Product", "/products"));
                break;
            case "rajnandini-bawne":
                name = "Rajnandini Bawne";
                services.add(createServiceData("Order", "/orders"));
                break;
            case "chetna-bendale":
                name = "Chetna Bendale";
                services.add(createServiceData("Store", "/stores"));
                break;
            case "yash-mukkawar":
                name = "Yash Mukkawar";
                services.add(createServiceData("Shipment", "/shipments"));
                break;
            case "ranjeet-patil":
                name = "Ranjeet Patil";
                services.add(createServiceData("Inventory", "/inventory"));
                break;
            default:
                return "redirect:/";
        }
        
        model.addAttribute("memberName", name);
        model.addAttribute("services", services);
        return "member-details";
    }
 
    private Map<String, Object> createServiceData(String serviceName, String url) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", serviceName);
        data.put("url", url);
        data.put("apis", generateMockApis(serviceName, url));
        return data;
    }
 
    private List<Map<String, String>> generateMockApis(String service, String uiBase) {
        String base = "/api/" + service.toLowerCase();
        List<Map<String, String>> apis = new ArrayList<>();
        
        apis.add(createApi("GET", base, "List all " + service + "s", uiBase));
        apis.add(createApi("GET", base + "/{id}", "Get " + service + " details by ID", null));
        apis.add(createApi("POST", base, "Create a new " + service, uiBase + "/new"));
        apis.add(createApi("PUT", base + "/{id}", "Update existing " + service, null));
        apis.add(createApi("DELETE", base + "/{id}", "Delete a " + service, null));
        apis.add(createApi("GET", base + "/search", "Search " + service + "s with parameters", uiBase));
        apis.add(createApi("GET", base + "/count", "Get total count of " + service + "s", null));
        apis.add(createApi("PATCH", base + "/{id}/status", "Update " + service + " status", null));
        apis.add(createApi("GET", base + "/recent", "List recently modified " + service + "s", uiBase));
        apis.add(createApi("GET", base + "/export", "Export " + service + " data to CSV/Excel", null));
        
        return apis;
    }
 
    private Map<String, String> createApi(String method, String uri, String desc, String uiLink) {
        Map<String, String> api = new HashMap<>();
        api.put("method", method);
        api.put("uri", uri);
        api.put("description", desc);
        if (uiLink != null) {
            api.put("uiLink", uiLink);
        }
        return api;
    }
}