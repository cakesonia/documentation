package com.prychko.lab3.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.prychko.lab3.web.rest.TestUtil;

public class InterviewerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Interviewer.class);
        Interviewer interviewer1 = new Interviewer();
        interviewer1.setId(1L);
        Interviewer interviewer2 = new Interviewer();
        interviewer2.setId(interviewer1.getId());
        assertThat(interviewer1).isEqualTo(interviewer2);
        interviewer2.setId(2L);
        assertThat(interviewer1).isNotEqualTo(interviewer2);
        interviewer1.setId(null);
        assertThat(interviewer1).isNotEqualTo(interviewer2);
    }
}
