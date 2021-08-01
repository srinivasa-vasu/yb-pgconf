package io.humourmind.todo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import io.ipdata.client.Ipdata;
import io.ipdata.client.model.IpdataModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "location")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class FindLocationConfig {
  private static final String DEFAULT_CODE = "AS";
  private boolean trigger;
  private String key;

  public String getGeoCode() {
    if (trigger) {
      try {
        URL url = new URL("https://api.ipdata.co");
        IpdataModel model = Ipdata.builder().url(url).key(key).get().ipdata(getPublicIP());
        return model.continentCode();
      } catch (Exception remoteEx) {
        // do nothing
      }
    }
    return DEFAULT_CODE;
  }

  private String getPublicIP() throws IOException {
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()))) {
      return br.readLine();
    }
  }
}
