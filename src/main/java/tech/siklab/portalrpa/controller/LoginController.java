package tech.siklab.portalrpa.controller;

import tech.siklab.portalrpa.model.Usuario;
import tech.siklab.portalrpa.repository.UsuarioRepository;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("portalrpa/login");
        Usuario usuario = new Usuario();
        mv.addObject("usuario", usuario);
        return mv;
    }

    @RequestMapping(value = "/logar", method = RequestMethod.POST)
    public ModelAndView form(@ModelAttribute(name = "usuario") Usuario usuario, HttpSession session, BindingResult result, RedirectAttributes attributes) {
        
        // Busca o usuario no banco e valida se existe.
        usuario = usuarioRepository.findByLoginAndSenha(usuario.getLogin(), usuario.getSenha());
        
        if (Objects.nonNull(usuario)) {
            // Imputa o objeto na sessão
            session.setAttribute("usuarioLogado", usuario);
            ModelAndView mv = new ModelAndView("portalrpa/index");
            mv.addObject("usuarioLogado", usuario);
            return mv;
        }

        // Invoca um erro caso possua usuario ou senha invalidos
        ModelAndView mv = new ModelAndView("portalrpa/login");
        mv.addObject("msgErro", "Usuario ou senha inválidos!");
        return mv;
    }

    @RequestMapping(value = "/logar", method = RequestMethod.GET)
    public ModelAndView listUsuario(HttpServletResponse response, HttpSession session) {

        // Valida na sessão se ja possui o atributo de usuario e redireciona ou cria o model para página inicial do sistema
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (Objects.nonNull(usuarioLogado)) {
            ModelAndView mv = new ModelAndView("portalrpa/index");
            mv.addObject("usuarioLogado", usuarioLogado);
            return mv;
        }

        // Restaura a página de login pois ainda não esta logado.
        try {
            response.sendRedirect("/");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {

        // Remove o objeto na sessão
        session.removeAttribute("usuarioLogado");
        return "redirect:/login";
    }

}
