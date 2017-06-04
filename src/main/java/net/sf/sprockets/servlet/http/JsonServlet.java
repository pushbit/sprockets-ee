/*
 * Copyright 2013-2015 pushbit <pushbit@gmail.com>
 *
 * This file is part of Sprockets.
 *
 * Sprockets is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Sprockets is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Sprockets. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.sprockets.servlet.http;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.sf.sprockets.lang.Classes;

/**
 * Deserialises POSTed/PUT JSON objects and serialises your objects to the response in JSON format.
 * Override any of the {@code json*} methods to handle requests. The Content-Type response header is
 * set to {@code application/json; charset=UTF-8}.
 *
 * @param <I>
 *            type of JSON object that will be POSTed/PUT and deserialised as the input object. Can
 *            be Void to skip deserialisation.
 * @param <O>
 *            type of object that will be serialised to the response in JSON format. Can be Void to
 *            skip serialisation.
 */
public abstract class JsonServlet<I, O> extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

	private final Type mIn;
	private final Type mOut;
	private final Gson mGson;

	public JsonServlet() {
		Type[] types = Classes.getTypeArguments(getClass(), "I", "O");
		mIn = types[0];
		mOut = types[1];
		mGson = getGson();
	}

	/**
	 * Override to provide a custom configuration.
	 */
	protected Gson getGson() {
		return new Gson();
	}

	/**
	 * Calls {@link #jsonGet(HttpServletRequest, HttpServletResponse)}.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE); // must call before getWriter, which subclass may call
		serialise(jsonGet(req, resp), resp);
	}

	/**
	 * Handle a GET request.
	 *
	 * @return object to serialise to the response in JSON format or null
	 */
	protected O jsonGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		return null;
	}

	/**
	 * Calls {@link #jsonPost(Object, HttpServletRequest, HttpServletResponse)}.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		serialise(jsonPost(deserialise(req), req, resp), resp);
	}

	/**
	 * Handle a POST request. The POSTed JSON object is deserialised to {@code in}.
	 *
	 * @param in
	 *            null if type {@code I} is Void
	 * @return object to serialise to the response in JSON format or null
	 */
	protected O jsonPost(I in, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		return null;
	}

	/**
	 * Calls {@link #jsonPut(Object, HttpServletRequest, HttpServletResponse)}.
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		serialise(jsonPut(deserialise(req), req, resp), resp);
	}

	/**
	 * Handle a PUT request. The PUT JSON object is deserialised to {@code in}.
	 *
	 * @param in
	 *            null if type {@code I} is Void
	 * @return object to serialise to the response in JSON format or null
	 * @since 2.1.0
	 */
	protected O jsonPut(I in, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		return null;
	}

	/**
	 * Calls {@link #jsonDelete(HttpServletRequest, HttpServletResponse)}.
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		serialise(jsonDelete(req, resp), resp);
	}

	/**
	 * Handle a DELETE request.
	 *
	 * @return object to serialise to the response in JSON format or null
	 * @since 2.1.0
	 */
	protected O jsonDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		return null;
	}

	/**
	 * Deserialise the input object from the request.
	 *
	 * @return null if type {@code I} is Void
	 */
	@SuppressWarnings("unchecked")
	private I deserialise(HttpServletRequest req) throws IOException {
		return mIn != Void.class ? (I) mGson.fromJson(req.getReader(), mIn) : null;
	}

	/**
	 * Serialise the output object to the response in JSON format.
	 */
	private void serialise(O out, HttpServletResponse resp) throws IOException {
		mGson.toJson(out, mOut, resp.getWriter());
	}
}
