package net.github.rtc.quiz.controller;

import net.github.rtc.quiz.Application;
import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Difficulty;
import net.github.rtc.quiz.model.Question;
import net.github.rtc.quiz.service.QuestionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    QuestionController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void findAllTest() throws Exception {
        final Question question1 = createQuestion();
        final Question question2  = createQuestion();
        when(questionService.findAll()).thenReturn(Arrays.asList(question1, question2));
        mockMvc.perform(get("/question"))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/json;charset=UTF-8"))
          .andExpect(jsonPath("$", hasSize(2)))
          .andExpect(jsonPath("$[0]._id").value("ID"))
          .andExpect(jsonPath("$[0].text").value("Question1"))
          .andExpect(jsonPath("$[1]._id").value("ID"))
          .andExpect(jsonPath("$[1].text").value("Question1"));
        verify(questionService, times(1)).findAll();
        verifyNoMoreInteractions(questionService);
    }

    private Question createQuestion(){
        Question question = new Question();
        question.set_id("ID");
        question.setText("Question1");
        question.setDifficulty(Difficulty.EASY);
        question.setAnswers(new ArrayList<Answer>());
        return question;
    }



}
