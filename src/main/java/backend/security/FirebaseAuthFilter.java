package backend.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        // Por ahora solamente dejamos libre /public
        return request
                .getRequestURI()
                .equals("/public");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // =========================================
        // PERMITIR PREFLIGHT CORS
        // =========================================

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        // =========================================
        // OBTENER AUTHORIZATION
        // =========================================

        String authorizationHeader =
                request.getHeader("Authorization");


        // =========================================
        // COMPROBAR QUE EXISTA TOKEN
        // =========================================

        if (
                authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")
        ) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{\"error\":\"Token no proporcionado\"}"
            );

            return;
        }


        // Quitar "Bearer "
        String token =
                authorizationHeader.substring(7);


        try {

            // =====================================
            // VALIDAR JWT FIREBASE
            // =====================================

            FirebaseToken decodedToken =
                    FirebaseAuth
                            .getInstance()
                            .verifyIdToken(token);


            // =====================================
            // GUARDAR DATOS DEL USUARIO
            // =====================================

            request.setAttribute(
                    "firebaseUid",
                    decodedToken.getUid()
            );

            request.setAttribute(
                    "firebaseEmail",
                    decodedToken.getEmail()
            );


            // =====================================
            // PROTEGER RUTAS /admin
            // =====================================

            if (
                    request
                            .getRequestURI()
                            .startsWith("/admin")
            ) {

                Object rolesObject =
                        decodedToken
                                .getClaims()
                                .get("roles");

                boolean isAdmin = false;


                if (rolesObject instanceof List<?> roles) {

                    isAdmin =
                            roles.contains("Admin");
                }


                if (!isAdmin) {

                    response.setStatus(
                            HttpServletResponse.SC_FORBIDDEN
                    );

                    response.setContentType(
                            "application/json"
                    );

                    response.getWriter().write(
                            "{\"error\":\"Acceso denegado: se requiere rol Admin\"}"
                    );

                    return;
                }
            }


            // =====================================
            // TOKEN VÁLIDO
            // =====================================

            filterChain.doFilter(
                    request,
                    response
            );


        } catch (FirebaseAuthException e) {

            // =====================================
            // TOKEN INVÁLIDO
            // =====================================

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{\"error\":\"Token Firebase inválido o expirado\"}"
            );
        }
    }
}