package com.prychko.lab3.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.prychko.lab3.web.rest.TestUtil;

public class VacancyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vacancy.class);
        Vacancy vacancy1 = new Vacancy();
        vacancy1.setId(1L);
        Vacancy vacancy2 = new Vacancy();
        vacancy2.setId(vacancy1.getId());
        assertThat(vacancy1).isEqualTo(vacancy2);
        vacancy2.setId(2L);
        assertThat(vacancy1).isNotEqualTo(vacancy2);
        vacancy1.setId(null);
        assertThat(vacancy1).isNotEqualTo(vacancy2);
    }
}
