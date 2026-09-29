package backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() throws IOException {

        if (FirebaseApp.getApps().isEmpty()) {

            String firebaseCredentialsPath =
                    System.getenv("FIREBASE_CREDENTIALS");

            if (firebaseCredentialsPath == null ||
                firebaseCredentialsPath.isBlank()) {

                throw new IllegalStateException(
                    "FIREBASE_CREDENTIALS no está configurada"
                );
            }

            FileInputStream serviceAccount =
                    new FileInputStream(firebaseCredentialsPath);

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(
                                GoogleCredentials.fromStream(
                                    serviceAccount
                                )
                            )
                            .setProjectId("pedidos360-f096d")
                            .build();

            FirebaseApp.initializeApp(options);

            System.out.println(
                "Firebase Admin inicializado correctamente"
            );
        }
    }
}