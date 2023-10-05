package com.religate.gstbills.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.religate.gstbills.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceItemsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItems.class);
        InvoiceItems invoiceItems1 = new InvoiceItems();
        invoiceItems1.setId(1L);
        InvoiceItems invoiceItems2 = new InvoiceItems();
        invoiceItems2.setId(invoiceItems1.getId());
        assertThat(invoiceItems1).isEqualTo(invoiceItems2);
        invoiceItems2.setId(2L);
        assertThat(invoiceItems1).isNotEqualTo(invoiceItems2);
        invoiceItems1.setId(null);
        assertThat(invoiceItems1).isNotEqualTo(invoiceItems2);
    }
}
