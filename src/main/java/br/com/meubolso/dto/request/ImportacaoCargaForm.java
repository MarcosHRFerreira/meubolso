package br.com.meubolso.dto.request;

import br.com.meubolso.enums.CategoriaFinanceira;
import br.com.meubolso.validations.ValidAnomesrefForCartaoCredito;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import br.com.meubolso.exception.ErrorMessages;

@ValidAnomesrefForCartaoCredito
public class ImportacaoCargaForm {

    @Schema(description = "Arquivo CSV", type = "string", format = "binary")
    @NotNull(message = ErrorMessages.FILE_REQUIRED)
    private MultipartFile file;

    @Schema(description = "Categoria financeira")
    @NotNull(message = ErrorMessages.TIPO_REQUIRED)
    private CategoriaFinanceira tipo;

    @Schema(description = "ID da conta corrente destino")
    @NotNull(message = ErrorMessages.CONTA_ID_REQUIRED)
    private Long contaId;

    @Schema(
        description = "Referência da fatura (YYYY-MM). Obrigatório para CARTAOCREDITO",
        pattern = "^\\d{4}-(0[1-9]|1[0-2])$",
        example = "2025-01"
    )
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = ErrorMessages.ANOMESREF_INVALID_FORMAT)
    private String anomesref;

    public CategoriaFinanceira getTipo() {
        return tipo;
    }

    public void setTipo(CategoriaFinanceira tipo) {
        this.tipo = tipo;
    }

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public String getAnomesref() {
        return anomesref;
    }

    public void setAnomesref(String anomesref) {
        this.anomesref = anomesref;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}