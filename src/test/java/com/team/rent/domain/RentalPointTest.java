package com.team.rent.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.team.rent.web.rest.TestUtil;

public class RentalPointTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentalPoint.class);
        RentalPoint rentalPoint1 = new RentalPoint();
        rentalPoint1.setId(1L);
        RentalPoint rentalPoint2 = new RentalPoint();
        rentalPoint2.setId(rentalPoint1.getId());
        assertThat(rentalPoint1).isEqualTo(rentalPoint2);
        rentalPoint2.setId(2L);
        assertThat(rentalPoint1).isNotEqualTo(rentalPoint2);
        rentalPoint1.setId(null);
        assertThat(rentalPoint1).isNotEqualTo(rentalPoint2);
    }
}
