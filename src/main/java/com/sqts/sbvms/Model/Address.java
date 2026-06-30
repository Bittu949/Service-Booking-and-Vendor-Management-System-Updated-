package com.sqts.sbvms.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "Address",
        description = "Represents a postal address."
)
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

    @Schema(
            description = "Primary address line.",
            example = "Flat 101, ABC Residency"
    )
    @NotBlank
    private String addressLine1;

    @Schema(
            description = "Secondary address line (optional).",
            example = "Near City Mall"
    )
    private String addressLine2;

    @Schema(
            description = "City name.",
            example = "Pune"
    )
    @NotBlank
    private String city;

    @Schema(
            description = "State name.",
            example = "Maharashtra"
    )
    @NotBlank
    private String state;

    @Schema(
            description = "Postal PIN code.",
            example = "411001"
    )
    @NotBlank
    private String pincode;
}