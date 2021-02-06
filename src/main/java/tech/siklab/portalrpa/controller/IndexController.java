package tech.siklab.portalrpa.controller;

import tech.siklab.portalrpa.model.Usuario;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping("/")
    public ModelAndView index(HttpSession session) {
        ModelAndView mv = new ModelAndView("portalrpa/index");
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        mv.addObject("usuarioLogado", usuarioLogado);
        return mv;
    }
}
