package medplanner.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testUsuarioValidations() {
        Usuario usuario = new Usuario();
        usuario.setUsername("valid.email@example.com");
        usuario.setPassword("password");
        usuario.setNome("Nome Valido");
        usuario.setCpf("70490670083");
        usuario.setCargo(Usuario.Cargo.ADMINISTRADOR);
        usuario.setSituacao("A");

        assertEquals("valid.email@example.com", usuario.getUsername());
        assertEquals("password", usuario.getPassword());
        assertEquals("Nome Valido", usuario.getNome());
        assertEquals("70490670083", usuario.getCpf());
        assertEquals(Usuario.Cargo.ADMINISTRADOR, usuario.getCargo());
        assertEquals("A", usuario.getSituacao());
    }

    @Test
    void testUsuarioAuthorities() {
        Usuario usuario = new Usuario();
        usuario.setCargo(Usuario.Cargo.MEDICO);

        assertTrue(usuario.getAuthorities().contains(new SimpleGrantedAuthority("MEDICO")));
    }

}
