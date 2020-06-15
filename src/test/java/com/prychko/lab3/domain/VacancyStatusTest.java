package com.prychko.lab3.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.prychko.lab3.web.rest.TestUtil;

public class VacancyStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VacancyStatus.class);
        VacancyStatus vacancyStatus1 = new VacancyStatus();
        vacancyStatus1.setId(1L);
        VacancyStatus vacancyStatus2 = new VacancyStatus();
        vacancyStatus2.setId(vacancyStatus1.getId());
        assertThat(vacancyStatus1).isEqualTo(vacancyStatus2);
        vacancyStatus2.setId(2L);
        assertThat(vacancyStatus1).isNotEqualTo(vacancyStatus2);
        vacancyStatus1.setId(null);
        assertThat(vacancyStatus1).isNotEqualTo(vacancyStatus2);
    }
}
