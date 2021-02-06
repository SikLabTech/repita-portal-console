package tech.siklab.portalrpa.controller;

import tech.siklab.portalrpa.model.CronogramaExecutando;
import tech.siklab.portalrpa.model.Usuario;
import tech.siklab.portalrpa.repository.CronogramaExecutandoRepository;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CronogramaExecutandoController {
    
    @Autowired
    private CronogramaExecutandoRepository cronogramaExecutandoRepository;

    @RequestMapping("/cronogramasExecutando")
    public ModelAndView cronogramasExecutando(HttpSession session) {
        
        ModelAndView mv = new ModelAndView("portalrpa/cronogramasExecutando");

        List<CronogramaExecutando> cronogramasExecutando = cronogramaExecutandoRepository.findAll();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("cronogramasExecutando", cronogramasExecutando);

        return mv;
    }

    @RequestMapping("/visualizarCronogramaExecutando")
    public ModelAndView visualizarCronogramaExecutando(Long id, HttpSession session) {
        
        ModelAndView mv = new ModelAndView("portalrpa/visualizarCronogramaExecutando");

        CronogramaExecutando cronogramaExecutando = cronogramaExecutandoRepository.findById(id);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("cronogramaExecutando", cronogramaExecutando);

        return mv;
    }
}
