import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONObject;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JTextField cityTextField;
    private JLabel temperatureLabel;
    private JLabel weatherConditionLabel;
    private JLabel humidityLabel;
    private JLabel windspeedLabel;
    private JLabel weatherIconLabel;
    private JLabel humidityIconLabel;
    private JLabel windspeedIconLabel;

    private static final String IMAGE_DIR = "WeatherAppGUI-Java-main/out/production/WeatherAppGUI/assets/";

    public WeatherAppGui() {
        setTitle("WEATHER APP");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel for city input
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(Color.WHITE);

        JLabel cityLabel = new JLabel("LOCATION:");
        cityLabel.setForeground(Color.BLACK);
        cityLabel.setFont(new Font("Serif", Font.BOLD, 24));

        cityTextField = new JTextField(15);
        cityTextField.setFont(new Font("Serif", Font.BOLD, 24));

        JButton searchButton = new JButton(new ImageIcon(new ImageIcon(IMAGE_DIR + "search.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        searchButton.setBackground(Color.WHITE);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String city = cityTextField.getText();
                    JSONObject weatherData = WeatherApp.getWeatherData(city);
                    displayWeatherData(weatherData);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(WeatherAppGui.this, "ERROR FETCHING WEATHER DATA", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        topPanel.add(cityLabel);
        topPanel.add(cityTextField);
        topPanel.add(searchButton);

        // Main panel for weather display
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage backgroundImage = ImageIO.read(new File(IMAGE_DIR + "background.jpg"));
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        weatherIconLabel = new JLabel(new ImageIcon(new ImageIcon(IMAGE_DIR + "default.png").getImage().getScaledInstance(105, 105, Image.SCALE_SMOOTH)));
        mainPanel.add(weatherIconLabel, gbc);

        gbc.gridy++;
        temperatureLabel = new JLabel("TEMPERATURE: ");
        temperatureLabel.setFont(new Font("Serif", Font.BOLD, 24));
        temperatureLabel.setForeground(Color.BLACK);
        mainPanel.add(temperatureLabel, gbc);

        gbc.gridy++;
        weatherConditionLabel = new JLabel("WEATHER CONDITION: ");
        weatherConditionLabel.setFont(new Font("Serif", Font.BOLD, 24));
        weatherConditionLabel.setForeground(Color.BLACK);
        mainPanel.add(weatherConditionLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        humidityLabel = new JLabel("HUMIDITY: ");
        humidityLabel.setFont(new Font("Serif", Font.BOLD, 24));
        humidityLabel.setForeground(Color.BLACK);
        mainPanel.add(humidityLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        humidityIconLabel = new JLabel(new ImageIcon(new ImageIcon(IMAGE_DIR + "humidity.png").getImage().getScaledInstance(52, 52, Image.SCALE_SMOOTH)));
        mainPanel.add(humidityIconLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        windspeedLabel = new JLabel("WINDSPEED: ");
        windspeedLabel.setFont(new Font("Serif", Font.BOLD, 24));
        windspeedLabel.setForeground(Color.BLACK);
        mainPanel.add(windspeedLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        windspeedIconLabel = new JLabel(new ImageIcon(new ImageIcon(IMAGE_DIR + "windspeed.png").getImage().getScaledInstance(52, 52, Image.SCALE_SMOOTH)));
        mainPanel.add(windspeedIconLabel, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void displayWeatherData(JSONObject weatherData) {
        temperatureLabel.setText("TEMPERATURE: " + weatherData.get("temperature") + " °C");
        weatherConditionLabel.setText("WEATHER CONDITION: " + weatherData.get("weather_condition"));
        humidityLabel.setText("HUMIDITY: " + weatherData.get("humidity") + " %");
        windspeedLabel.setText("WINDSPEED: " + weatherData.get("windspeed") + " m/s");

        // Set weather icon based on condition
        String weatherCondition = (String) weatherData.get("weather_condition");
        String iconPath = getIconPath(weatherCondition);
        weatherIconLabel.setIcon(new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(105, 105, Image.SCALE_SMOOTH)));
    }

    private String getIconPath(String weatherCondition) {
        // Map weather conditions to icons
        if (weatherCondition.contains("clear")) {
            return IMAGE_DIR + "clear.png";
        } else if (weatherCondition.contains("clouds")) {
            return IMAGE_DIR + "cloudy.png";
        } else if (weatherCondition.contains("rain")) {
            return IMAGE_DIR + "rain.png";
        } else {
            return IMAGE_DIR + "default.png";
        }
    }
}
