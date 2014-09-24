package net.github.rtc.quiz;

import net.github.rtc.quiz.dao.impl.QuestionDAO;
import net.github.rtc.quiz.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @Autowired
    private QuestionDAO questionDAO;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showAll() {
        ModelAndView modelAndView = new ModelAndView("hello");
        questionDAO.insert(new Question("Bla Bla Bla"));
        return modelAndView;
    }
}
