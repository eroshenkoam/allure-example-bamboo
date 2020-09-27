package io.eroshenkoam.allure;

import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

public class GoogleAuth {

    @Test
    @AllureId("4")
    @DisplayName("Closing new issue for authorized user")
    @Tags({@Tag("web"), @Tag("regress")})
    @Story("Close existing issue")
    @UrlPath("/{org}/{repo}/issues")
    @Feature("Issues")
    @PagePath("/{org}/{repo}/issues")
    @Owner("eroshenkoam")
    public void testAuth() {
    }

}
