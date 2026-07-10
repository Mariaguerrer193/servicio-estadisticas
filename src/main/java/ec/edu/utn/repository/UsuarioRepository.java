package ec.edu.utn.repository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import ec.edu.utn.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    // Formato de correo válido (algo@algo.algo)
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    // Al menos: 1 minúscula, 1 mayúscula, 1 símbolo, 8+ caracteres
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$");

    // Listar todos
    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    // Buscar por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Usuario.class, id));
    }

    // Buscar por username (útil para login más adelante)
    public Optional<Usuario> buscarPorUsername(String username) {
        List<Usuario> resultado = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class)
                .setParameter("username", username)
                .getResultList();
        return resultado.stream().findFirst();
    }

    // Crear, con validaciones y hash de contraseña
    @Transactional
    public Usuario crear(Usuario u, String passwordPlano) {
        if (!EMAIL_PATTERN.matcher(u.getEmail()).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }
        if (!PASSWORD_PATTERN.matcher(passwordPlano).matches()) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un símbolo");
        }
        String hash = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
        u.setPasswordHash(hash);
        em.persist(u);
        return u;
    }

    // Verifica username + contraseña en texto plano. Devuelve el usuario si son correctos.
    public Optional<Usuario> verificarCredenciales(String username, String passwordPlano) {
        Optional<Usuario> usuario = buscarPorUsername(username);
        if (usuario.isPresent() && BCrypt.checkpw(passwordPlano, usuario.get().getPasswordHash())) {
            return usuario;
        }
        return Optional.empty();
    }
}