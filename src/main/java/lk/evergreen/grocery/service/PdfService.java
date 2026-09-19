package lk.evergreen.grocery.service;

import lk.evergreen.grocery.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Generates a PDF byte array from an Order entity using Thymeleaf templates and Flying Saucer rendering.
     *
     * @param order the Order entity to bind to the template
     * @return PDF file contents as a byte array
     */
    public byte[] generateInvoicePdf(Order order) {
        try {
            // 1. Prepare Thymeleaf Context and bind the order model
            Context context = new Context();
            context.setVariable("order", order);

            // 2. Render HTML string with evaluated Thymeleaf tags
            String htmlContent = templateEngine.process("invoice-template", context);

            // 3. Convert HTML to PDF using Flying Saucer ITextRenderer
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error rendering PDF invoice for Order #" + order.getId() + ": " + e.getMessage(), e);
        }
    }
}