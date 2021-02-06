package tech.siklab.portalrpa.controller;

import tech.siklab.portalrpa.model.Usuario;
import tech.siklab.portalrpa.repository.UsuarioRepository;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @RequestMapping("/usuarios")
    public ModelAndView usuarios(HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo().equals("admin")) {
            ModelAndView mv = new ModelAndView("portalrpa/usuarios");
            List<Usuario> usuarios = usuarioRepository.findAll();

            mv.addObject("usuarioLogado", usuarioLogado);
            mv.addObject("usuarios", usuarios);
            return mv;
        }

        ModelAndView mv = new ModelAndView("portalrpa/index");

        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("msgErro", "Você não é um administrador");

        return mv;
    }

    @RequestMapping("/editarUsuario")
    public ModelAndView editarUsuario(Long id, HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo().equals("admin")) {
            ModelAndView mv = new ModelAndView("portalrpa/editarUsuario");
            Usuario usuario = usuarioRepository.findById(id);

            mv.addObject("usuarioLogado", usuarioLogado);
            mv.addObject("usuario", usuario);

            return mv;
        }

        ModelAndView mv = new ModelAndView("portalrpa/index");

        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("msgErro", "Você não é um administrador");

        return mv;
    }

    @RequestMapping("/adicionarUsuario")
    public ModelAndView adicionarUsuario(HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo().equals("admin")) {
            ModelAndView mv = new ModelAndView("portalrpa/adicionarUsuario");
            Usuario newUsuario = new Usuario();

            mv.addObject("usuarioLogado", usuarioLogado);
            mv.addObject("newUsuario", newUsuario);

            return mv;
        }

        ModelAndView mv = new ModelAndView("portalrpa/index");

        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("msgErro", "Você não é um administrador");
        return mv;
    }

    @RequestMapping("/excluirUsuario")
    public ModelAndView excluirUsuario(Long id, HttpSession session, HttpServletResponse response) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo().equals("admin")) {

            Usuario usuario = usuarioRepository.findById(id);
            usuarioRepository.delete(usuario);

            try {
                response.sendRedirect("/usuarios");
            } catch (IOException ex) {
                Logger.getLogger(CronogramaController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }

        ModelAndView mv = new ModelAndView("portalrpa/index");

        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("msgErro", "Você não é um administrador");
        return mv;
    }

    @RequestMapping(value = "/saveUsuario", method = RequestMethod.POST)
    public ModelAndView form(@Valid Usuario usuario, HttpSession session, HttpServletResponse response, BindingResult result, RedirectAttributes attributes) {
        
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo().equals("admin")) {
            
            usuarioRepository.save(usuario);
            
            try {
                response.sendRedirect("/usuarios");
            } catch (IOException ex) {
                Logger.getLogger(CronogramaController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
        }

        ModelAndView mv = new ModelAndView("portalrpa/index");

        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("msgErro", "Você não é um administrador");
        return mv;
    }
}
