package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import persistence.commons.ConnectionProvider;
import modelos.Itinerario;

import modelos.nullobjects.NullItinerario;
import persistence.ItinerarioDAO;
import persistence.commons.MissingDataException;

public class ItinerarioDAOImpl implements ItinerarioDAO {

	public int update(Itinerario itinerario) {

		try {
			Connection connection = ConnectionProvider.getConnection();
			String sql = "UPDATE ITINERARIO SET productosComprados = ?, horasNecesarias = ?, puntosGastados = ? WHERE USUARIO = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, itinerario.getProductos());
			statement.setDouble(2, itinerario.getHorasNecesarias());
			statement.setDouble(3, itinerario.getPuntos());
			statement.setString(4, itinerario.getUsuario());
			int rows = statement.executeUpdate();

			return rows;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

	public List<Itinerario> findAll() {
		try {
			Connection connection = ConnectionProvider.getConnection();
			String sql = "SELECT * FROM itinerario";
			PreparedStatement statement = connection.prepareStatement(sql);

			ResultSet result = statement.executeQuery();

			List<Itinerario> lista = new LinkedList<Itinerario>();

			while (result.next()) {
				lista.add(toItinerario(result));
			}

			return lista;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

	private Itinerario toItinerario(ResultSet result) throws SQLException {

		String usuario = result.getString("usuario");
		String productos = result.getString("productosComprados");
		Double horasNecesarias = result.getDouble("horasNecesarias");
		Double puntos = result.getDouble("puntosGastados");

		return new Itinerario(usuario, productos, horasNecesarias, puntos);
	}

	@Override
	public Itinerario find(Integer id) {
		try {
			String sql = "SELECT * FROM ITINERARIO WHERE ID_ITINERARIO = ?";
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultados = statement.executeQuery();

			Itinerario itinerario = NullItinerario.build();

			if (resultados.next()) {
				itinerario = toItinerario(resultados);
			}

			return itinerario;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

	@Override
	public int insert(Itinerario itinerario) {
		try {
			Connection connection = ConnectionProvider.getConnection();
			String sql = "INSERT INTO ITINERARIO (USUARIO, PRODUCTOSCOMPRADOS, HORASNECESARIAS, PUNTOSGASTADOS) VALUES (?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, itinerario.getUsuario());
			statement.setString(2, itinerario.getProductos());
			statement.setDouble(3, itinerario.getHorasNecesarias());
			statement.setDouble(4, itinerario.getPuntos());

			int rows = statement.executeUpdate();

			return rows;

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}

	@Override
	public int delete(Itinerario itinerario) {
		try {
			String sql = "DELETE FROM ITINERARIO WHERE USUARIO = ?";
			Connection conn = ConnectionProvider.getConnection();

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, itinerario.getUsuario());
			int rows = statement.executeUpdate();

			return rows;
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
	}
}
