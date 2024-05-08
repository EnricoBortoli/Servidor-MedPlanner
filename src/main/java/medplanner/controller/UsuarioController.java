package medplanner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import medplanner.model.Usuario;
import medplanner.repository.UsuarioRepository;

@RestController
@RequestMapping("usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository UsuarioRepository;

    @RequestMapping("/listar")
    public List<Usuario> listarUsuario(){
        return UsuarioRepository.findAll();
    }

    @GetMapping("/buscar")
    public ResponseEntity buscarUsuario(@RequestParam Map<String, String> parametros){
        if(parametros.isEmpty()){
            //TODO alterar os endPoints de buscar com parametro vazio para trazer um findAll em vez de mensagem de aviso.
            return ResponseEntity.badRequest().body("Parâmetro de pesquisa inválidos");
        }
        if(parametros.get("id") != null){
            return ResponseEntity.ok().body(UsuarioRepository.findAllByIdUsuario(parametros.get("id")));
        }
        if(parametros.get("nome") != null){
            return ResponseEntity.ok().body(UsuarioRepository.findAllByNomeUsuario((parametros.get("nome"))));
        }
        return ResponseEntity.badRequest().body("Parâmetro de pesquisa inválidos");
    }

    @PostMapping("/salvar")
    public ResponseEntity salvarUsuario(@RequestBody Usuario Usuario) {
        try{
            if(UsuarioRepository.findByUsername(Usuario.getUsername()) != null){
                return ResponseEntity.badRequest().build();
            }

            String passwordCriptografada = new BCryptPasswordEncoder().encode(Usuario.getPassword());
            Usuario.setPassword(passwordCriptografada);
            UsuarioRepository.save(Usuario);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.badRequest().body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        Usuario Usuario = UsuarioRepository.findById(id).orElse(null);
        
        if (Usuario != null) {
            UsuarioRepository.delete(Usuario);
            return ResponseEntity.ok("Usuario deletado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}