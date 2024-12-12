package top.yu717.sendmsg2.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/ip")
public class IpController {
    private final Map<String ,String> ipMap = new java.util.HashMap<>();

    @GetMapping("/cheng_ip")
    public String getClientIpAddress(HttpServletRequest request,@RequestParam String msg)  {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 处理 X-Forwarded-For 头中的多个 IP 地址
        if (ipAddress != null && ipAddress.contains(",")) {
            String[] ips = ipAddress.split(",");
            ipAddress = ips[0]; // 取第一个 IP 地址
        }
        String[] parts = msg.split(",");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Message does not contain a valid IPv6 address");
        }
        String ipv6 = parts[1];

        String key = msg + ":" + request.getRemoteAddr();
        ipMap.put(key,ipv6);
        System.out.println(msg);
        System.out.println("Remote Address: " + request.getRemoteAddr());
        System.out.println("Remote Host: " + request.getRemoteHost());
        System.out.println(ipMap);
        return ipAddress;
    }

    @GetMapping("/{msg}")
    public String trackIp(HttpServletRequest request,@PathVariable String msg) {
        System.out.println(msg);
        StringBuilder html = new StringBuilder();
        html.append("<html><head>");
        html.append("<script type=\"text/javascript\">");
        if(msg != null){
            String ipv6 = String.valueOf(getTrackV6(msg));
            if(ipv6.isEmpty()){
                html.append("</script>");
                html.append("</head><body>");
                html.append("<h1>Redirecting...</h1>");
                html.append("<p>你没有存入ip地址</p>");
                html.append("</body></html>");
                return html.toString();
            }
            html.append("setTimeout(function() { window.location.href = 'http://[").append(ipv6).append("]'; }, 0);");
            extracted(html);
        }else if(request.getRemoteAddr() != null){
            String ipv6 = String.valueOf(getTrackV6(request.getRemoteAddr()));
            if(ipv6.isEmpty()){
                html.append("</script>");
                html.append("</head><body>");
                html.append("<h1>Redirecting...</h1>");
                html.append("<p>你没有存入ip地址</p>");
                html.append("</body></html>");
            }else {
                html.append("setTimeout(function() { window.location.href = 'http://[").append(ipv6).append("]'; }, 0);");
                extracted(html);
            }
        }
        return html.toString();
    }

    private static void extracted(StringBuilder html) {
        html.append("</script>");
        html.append("</head><body>");
        html.append("<h1>Redirecting...</h1>");
        html.append("<p>You will be redirected to <a href=\"https://www.example.com\">Example.com</a> in 5 seconds.</p>");
        html.append("</body></html>");
    }


    public AtomicReference<String> getTrackV6(String msg){
        AtomicReference<String> v6 = new AtomicReference<>("");
        ipMap.forEach((key,value)->{
            // 如果key中包含msg
            if(key.contains(msg)){
                v6.set(value);
            }

        });
        return v6;
    }




}
