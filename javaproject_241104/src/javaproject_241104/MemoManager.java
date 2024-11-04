package javaproject_241104;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MemoManager extends JFrame {
	private JTextField titleField;
	private JTextArea contentArea;
	private JButton addButton, deleteButton, viewButton;
	private JList<String> memoList;
	private DefaultListModel<String> listModel;
	private MemoDAO dao;

	public MemoManager() {
		dao = new MemoDAO();

		setTitle("메모 프로그램");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel inputPanel = new JPanel(new BorderLayout());

		// 제목 필드와 라벨
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.add(new JLabel("제목"), BorderLayout.NORTH);
		titleField = new JTextField();
		titlePanel.add(titleField, BorderLayout.CENTER);

		// 내용 필드와 라벨
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(new JLabel("내용"), BorderLayout.NORTH);
		contentArea = new JTextArea(5, 20);
		contentPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

		// 메모 목록 필드와 라벨
		JPanel listPanel = new JPanel(new BorderLayout());
		listPanel.add(new JLabel("메모 목록"), BorderLayout.NORTH);
		listModel = new DefaultListModel<>();
		memoList = new JList<>(listModel);
		listPanel.add(new JScrollPane(memoList), BorderLayout.CENTER);

		// Input 패널에 제목, 내용 패널 추가
		inputPanel.add(titlePanel, BorderLayout.NORTH);
		inputPanel.add(contentPanel, BorderLayout.CENTER);

		addButton = new JButton("메모추가");
		addButton.addActionListener(e -> {
			String title = titleField.getText();
			String content = contentArea.getText();
			MemoDTO memo = new MemoDTO(0, title, content);
			if (dao.addMemo(memo)) {
				JOptionPane.showMessageDialog(null, "메모가 추가되었습니다.");
				titleField.setText("");
				contentArea.setText("");
				viewMemoList(); // 메모 추가 후 목록 갱신
			} else {
				JOptionPane.showMessageDialog(null, "메모 추가 실패.");
			}
		});

		deleteButton = new JButton("삭제");
		deleteButton.addActionListener(e -> {
			int selectedIndex = memoList.getSelectedIndex();
			if (selectedIndex != -1) {
				String selectedValue = memoList.getSelectedValue();
				int id = Integer.parseInt(selectedValue.split(":")[0]);
				if (dao.deleteMemo(id)) {
					JOptionPane.showMessageDialog(null, "메모가 삭제되었습니다.");
					viewMemoList(); // 메모 삭제 후 목록 갱신
				} else {
					JOptionPane.showMessageDialog(null, "메모 삭제 실패.");
				}
			}
		});

		viewButton = new JButton("메모조회");
		viewButton.addActionListener(e -> {
			int selectedIndex = memoList.getSelectedIndex();
			if (selectedIndex != -1) {
				String selectedValue = memoList.getSelectedValue();
				int id = Integer.parseInt(selectedValue.split(":")[0]);
				MemoDTO memo = dao.getMemoById(id);
				if (memo != null) {
					titleField.setText(memo.getTitle());
					contentArea.setText(memo.getContent());
				}
			} else {
				JOptionPane.showMessageDialog(null, "조회할 메모를 선택하세요.");
			}
		});

		viewMemoList(); // 초기 화면에 메모 목록 표시

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(viewButton);

		// 프레임에 각 패널 추가
		add(inputPanel, BorderLayout.NORTH);
		add(listPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void viewMemoList() {
		listModel.clear(); // 기존 목록을 지움
		ArrayList<MemoDTO> memos = dao.viewMemo(); // DAO에서 메모 목록을 가져옴
		for (MemoDTO memo : memos) {
			listModel.addElement(memo.getId() + ": " + memo.getTitle()); // 목록에 추가
		}
	}

	public static void main(String[] args) {
		new MemoManager();
	}
}
