package googleAPI_pj;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GooglePlacesGourmetFinder extends JFrame {

    private static final String GOOGLE_API_KEY = "";

    private JTextField inputField;
    private DefaultListModel<String> stationListModel;
    private JEditorPane resultPane;

    private JCheckBox openNowCheckbox;
    private JComboBox<String> sortComboBox;

    private String selectedFoodType = "맛집";
    private GooglePlacesService apiService;

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        EventQueue.invokeLater(() -> {
            try { new GooglePlacesGourmetFinder().setVisible(true); } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public GooglePlacesGourmetFinder() {
        this.apiService = new GooglePlacesService(GOOGLE_API_KEY);

        setTitle("Google Places 맛집 검색기 (통합 리스트)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 950);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(UIConstants.COLOR_BG_GRAY);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BorderLayout(0, 20));
        setContentPane(contentPane);

        contentPane.add(createInputPanel(), BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout(0, 15));
        centerContainer.setBackground(UIConstants.COLOR_BG_GRAY);
        centerContainer.add(createFoodTypePanel(), BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        gridPanel.setBackground(UIConstants.COLOR_BG_GRAY);
        gridPanel.add(createStationListPanel());
        gridPanel.add(createResultAreaPanel());
        centerContainer.add(gridPanel, BorderLayout.CENTER);

        contentPane.add(centerContainer, BorderLayout.CENTER);
        contentPane.add(createControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(UIConstants.COLOR_BG_GRAY);
        inputField = new JTextField();
        inputField.setFont(UIConstants.FONT_NORMAL);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER_GRAY),
                new EmptyBorder(8, 10, 8, 10)
        ));
        inputField.addActionListener(e -> addStation());

        JButton btnAdd = createStyledButton("추가", UIConstants.COLOR_GOOGLE_BLUE);
        btnAdd.addActionListener(e -> addStation());

        panel.add(inputField, BorderLayout.CENTER);
        panel.add(btnAdd, BorderLayout.EAST);
        return panel;
    }

    private JPanel createFoodTypePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIConstants.COLOR_BG_GRAY);

        JLabel lbl = new JLabel("음식 종류 선택", SwingConstants.LEFT);
        lbl.setFont(UIConstants.FONT_BOLD);
        panel.add(lbl, BorderLayout.NORTH);

        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tagPanel.setBackground(UIConstants.COLOR_BG_GRAY);

        String[] types = {"전체", "한식", "일식", "중식", "양식", "카페", "술집", "고기", "치킨"};
        ButtonGroup group = new ButtonGroup();

        for (String t : types) {
            JToggleButton btn = new JToggleButton(t);
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setPreferredSize(new Dimension(80, 30));

            group.add(btn);
            tagPanel.add(btn);

            btn.addActionListener(e -> selectedFoodType = btn.getText().equals("전체") ? "맛집" : btn.getText());
            if (t.equals("전체")) btn.setSelected(true);
        }

        panel.add(tagPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStationListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(UIConstants.COLOR_BORDER_GRAY));

        JLabel lbl = new JLabel("검색 대상 역", SwingConstants.LEFT);
        lbl.setFont(UIConstants.FONT_BOLD);
        lbl.setBorder(new EmptyBorder(10, 10, 5, 10));
        panel.add(lbl, BorderLayout.NORTH);

        stationListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(stationListModel);
        list.setFont(UIConstants.FONT_NORMAL);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    if (index >= 0) stationListModel.remove(index);
                }
            }
        });

        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultAreaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(UIConstants.COLOR_BORDER_GRAY));

        JLabel lbl = new JLabel("검색 결과 (지도 포함)", SwingConstants.LEFT);
        lbl.setFont(UIConstants.FONT_BOLD);
        lbl.setBorder(new EmptyBorder(10, 10, 5, 10));
        panel.add(lbl, BorderLayout.NORTH);

        resultPane = new JEditorPane();
        resultPane.setContentType("text/html");
        resultPane.setText("<html><body style='font-family:Malgun Gothic; padding:10px; color:#555;'>상단에 역 이름을 입력하고 <b>[추가]</b> 버튼을 눌러주세요.<br>그 후 <b>[통합 검색 시작]</b>을 누르면 결과가 표시됩니다.</body></html>");
        resultPane.setEditable(false);

        resultPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try { Desktop.getDesktop().browse(e.getURL().toURI()); } catch (Exception ex) {}
            }
        });

        panel.add(new JScrollPane(resultPane), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(UIConstants.COLOR_BG_GRAY);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        optionPanel.setBackground(UIConstants.COLOR_BG_GRAY);

        openNowCheckbox = new JCheckBox("영업중만 보기");
        openNowCheckbox.setBackground(UIConstants.COLOR_BG_GRAY);
        openNowCheckbox.setForeground(new Color(0, 128, 0));

        String[] sortOptions = {"구글 추천순 (섞기)", "리뷰 많은 순", "평점 높은 순", "구글 추천순 (역별)"};
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.setBackground(Color.WHITE);

        optionPanel.add(openNowCheckbox);
        optionPanel.add(new JLabel(" 정렬:"));
        optionPanel.add(sortComboBox);

        JButton btnSearch = createStyledButton("통합 검색 시작", UIConstants.COLOR_GOOGLE_BLUE);
        btnSearch.addActionListener(e -> new Thread(this::runSearchProcess).start());

        panel.add(optionPanel, BorderLayout.CENTER);
        panel.add(btnSearch, BorderLayout.EAST);
        return panel;
    }

    private void addStation() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        for (String s : text.split(",")) {
            if (!s.trim().isEmpty() && !stationListModel.contains(s.trim())) {
                stationListModel.addElement(s.trim());
            }
        }
        inputField.setText("");
    }

    private void runSearchProcess() {
        if (GOOGLE_API_KEY.equals("YOUR_GOOGLE_API_KEY")) {
            JOptionPane.showMessageDialog(this, "API Key를 코드에 입력해주세요!", "설정 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (stationListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "검색할 역을 먼저 추가해주세요.");
            return;
        }

        SwingUtilities.invokeLater(() ->
                resultPane.setText("<html><body style='font-family:Malgun Gothic; padding:10px; color:blue;'>데이터 및 지도 로딩 중...</body></html>")
        );

        List<Restaurant> allResults = new ArrayList<>();
        StringBuilder errorMsg = new StringBuilder();

        boolean openNow = openNowCheckbox.isSelected();
        String selectedSort = (String) sortComboBox.getSelectedItem();

        for (int i = 0; i < stationListModel.size(); i++) {
            String station = stationListModel.get(i);
            try {
                List<Restaurant> items = apiService.fetchPlaces(station, selectedFoodType, openNow);
                allResults.addAll(items);
            } catch (Exception e) {
                e.printStackTrace();
                errorMsg.append(station).append(" 오류: ").append(e.getMessage()).append("<br>");
            }
        }

        apiService.sortRestaurants(allResults, selectedSort);

        String mapUrl = apiService.generateStaticMapUrl(allResults, 15);
        final String finalHtml = buildHtmlOutput(allResults, errorMsg.toString(), mapUrl);

        SwingUtilities.invokeLater(() -> {
            resultPane.setText(finalHtml);
            resultPane.setCaretPosition(0);
        });
    }

    private String buildHtmlOutput(List<Restaurant> items, String error, String mapUrl) {
        StringBuilder sb = new StringBuilder("<html><head><style>body{font-family:'Malgun Gothic'; font-size:12px;} .rank{color:red;font-weight:bold;} .tag{color:#008800;font-weight:bold;font-size:10px;} .price{color:#555;font-weight:bold;font-size:11px;margin-left:5px;}</style></head><body>");

        if (!error.isEmpty()) sb.append("<div style='color:red;'>").append(error).append("</div>");

        if (items.isEmpty() && error.isEmpty()) {
            sb.append("<div style='padding:10px;'><b>검색 결과가 없습니다.</b><br>영업중 필터를 끄거나 다른 검색어를 시도해보세요.</div>");
        } else {
            if (!mapUrl.isEmpty()) {
                sb.append("<div style='text-align:center; margin-bottom:15px;'>");
                sb.append("<img src='").append(mapUrl).append("' width='400' height='250' alt='지도'>");
                sb.append("</div>");
            }

            String sortOption = (String) sortComboBox.getSelectedItem();
            sb.append("<h3>통합 검색 결과 (").append(sortOption).append(")</h3>");
            for (int i = 0; i < items.size(); i++) sb.append(createItemHtml(items.get(i), i + 1));
        }

        sb.append("</body></html>");
        return sb.toString();
    }

    private String createItemHtml(Restaurant item, int rank) {
        String rankStyle = (rank <= 3) ? "font-size:14px; color:red;" : "font-size:12px; color:#555;";

        String priceStr = "";
        if (item.priceLevel > 0) {
            priceStr = "<span class='price'>" + "₩".repeat(item.priceLevel) + "</span>";
        }

        return String.format(
                "<div style='border-bottom:1px solid #eee; padding:8px; margin-bottom:5px;'>" +
                        "  <div><span style='font-weight:bold; %s'>%d.</span> <span class='tag'>[%s]</span> <a href='%s'>%s</a>%s</div>" +
                        "  <div style='font-size:10px; color:#666;'>%s</div>" +
                        "  <div><span style='color:orange;'>★</span> <b>%.1f</b> <span style='font-size:10px; color:blue;'>(리뷰 %d개)</span></div>" +
                        "</div>",
                rankStyle, rank, item.stationName.replace("역", ""), item.placeUrl, item.name, priceStr, item.address, item.rating, item.reviewCount
        );
    }

    private JButton createStyledButton(String text, Color c) {
        JButton b = new JButton(text);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setBorder(new EmptyBorder(8, 15, 8, 15));
        b.setFocusPainted(false);

        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(c.darker()); }
            public void mouseExited(MouseEvent e) { b.setBackground(c); }
        });

        return b;
    }
}


