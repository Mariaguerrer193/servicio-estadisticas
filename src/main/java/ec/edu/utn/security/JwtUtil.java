package ec.edu.utn.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ec.edu.utn.model.Usuario;

import java.util.Date;

public class JwtUtil {

    // Clave secreta para firmar los tokens (en un entorno real, esto iría en una variable de entorno, no en el código)
    private static final String SECRETO = "UTNGolMundial2026-ClaveSecretaServicioEstadisticas";

    // El token expira en 2 horas
    private static final long DURACION_MS = 2 * 60 * 60 * 1000;

    private static final Algorithm ALGORITMO = Algorithm.HMAC256(SECRETO);

    // Genera un token para un usuario autenticado, incluyendo su ID y su rol
    public static String generarToken(Usuario usuario) {
        return JWT.create()
                .withSubject(usuario.getUsername())
                .withClaim("userId", usuario.getId())
                .withClaim("rol", usuario.getRol().getNombre())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + DURACION_MS))
                .sign(ALGORITMO);
    }

    // Valida un token y lo decodifica. Lanza una excepción si es inválido o expiró.
    public static DecodedJWT validarToken(String token) {
        return JWT.require(ALGORITMO).build().verify(token);
    }
}