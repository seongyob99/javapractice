package javaproject_241104;

import java.sql.*;
import java.util.ArrayList;

public class MemoDAO {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "scott";
	String passwd = "tiger";

	public MemoDAO() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean addMemo(MemoDTO memo) {
		String query = "INSERT INTO memo (title, content) VALUES (?, ?)";
		try (Connection con = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, memo.getTitle());
			pstmt.setString(2, memo.getContent());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// viewMemo 메서드에서 매개변수 제거
	public ArrayList<MemoDTO> viewMemo() {
		ArrayList<MemoDTO> memoList = new ArrayList<>();
		String query = "SELECT * FROM memo";
		try (Connection con = DriverManager.getConnection(url, userid, passwd);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				memoList.add(new MemoDTO(id, title, content));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memoList;
	}

	public MemoDTO getMemoById(int id) {
		String query = "SELECT * FROM memo WHERE id = ?";
		try (Connection con = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String title = rs.getString("title");
					String content = rs.getString("content");
					return new MemoDTO(id, title, content);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean deleteMemo(int id) {
		String query = "DELETE FROM memo WHERE id = ?";
		try (Connection con = DriverManager.getConnection(url, userid, passwd);
				PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, id);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
