package tech.siklab.portalrpa.controller;

import tech.siklab.portalrpa.model.Cronograma;
import tech.siklab.portalrpa.model.Usuario;
import tech.siklab.portalrpa.repository.CronogramaRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CronogramaController {

    private static String uploadDirectory = System.getProperty("user.dir") + "/Documents/Projetos RPA";

    @Autowired
    private CronogramaRepository cronogramaRepository;

    @RequestMapping("/cronogramas")
    public ModelAndView cronogramas(HttpSession session) {

        ModelAndView mv = new ModelAndView("portalrpa/cronogramas");

        List<Cronograma> cronogramas = cronogramaRepository.findAll();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("cronogramas", cronogramas);

        return mv;
    }

    @RequestMapping("/editarCronograma")
    public ModelAndView editarCronograma(Long id, HttpSession session) {

        ModelAndView mv = new ModelAndView("portalrpa/editarCronograma");

        Cronograma cronograma = cronogramaRepository.findById(id);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("cronograma", cronograma);
        return mv;
    }

    @RequestMapping("/adicionarCronograma")
    public ModelAndView adicionarCronograma(HttpSession session) {

        ModelAndView mv = new ModelAndView("portalrpa/adicionarCronograma");

        Cronograma newCronograma = new Cronograma();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        mv.addObject("usuarioLogado", usuarioLogado);
        mv.addObject("newCronograma", newCronograma);
        return mv;
    }

    @RequestMapping(value = "/saveCronograma", method = RequestMethod.POST)
    public ModelAndView form(@RequestParam("file") MultipartFile file, @Valid Cronograma cronograma, HttpSession session, HttpServletResponse response, BindingResult result, RedirectAttributes attributes) {

        if(!file.isEmpty()){
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            try {
                if (!Files.exists(Paths.get(uploadDirectory))) {
                    Files.createDirectories(Paths.get(uploadDirectory));
                }
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            cronograma.setRobo(fileNameAndPath.toString());
        }

        cronogramaRepository.save(cronograma);

        try {
            response.sendRedirect("/cronogramas");
        } catch (IOException ex) {
            Logger.getLogger(CronogramaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @RequestMapping("/excluirCronograma")
    public String excluirCronograma(Long id) {

        cronogramaRepository.delete(cronogramaRepository.findById(id));

        return "redirect:/cronogramas";
    }
}
