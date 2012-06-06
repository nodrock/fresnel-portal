package cz.muni.fi.fresnelportal.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Utils class for getting base URL.
 * @author nodrock
 */
public class HttpUtils {
     public static String getBaseUrl(HttpServletRequest request) {
        String baseUrl;
        if ((request.getServerPort() == 80)
                || (request.getServerPort() == 443)) {
            baseUrl =
                    request.getScheme() + "://"
                    + request.getServerName()
                    + request.getContextPath();
        } else {
            baseUrl =
                    request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
        }
        return baseUrl;
    }
}
