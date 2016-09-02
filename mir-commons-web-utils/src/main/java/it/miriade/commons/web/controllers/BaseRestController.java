package it.miriade.commons.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import it.miriade.commons.collections.CollectionUtils;
import it.miriade.commons.utils.StringHandler;
import it.miriade.commons.web.Hateoas;
import it.miriade.commons.web.filters.CORS;

public abstract class BaseRestController implements Hateoas {

	private static final String RESPONSE_MESSAGE_KEY = "message";

	/**
	 * Metodo comune ausiliario per il protocollo HATEOAS. Alla chiamata di un
	 * metodo OPTIONS per una determinata URL, ritorna tutti i metodi
	 * implementati da quella determinata URL.
	 * 
	 * @param responce
	 * @param httpMethods
	 * @throws IOException
	 */
	@SuppressWarnings("all")
	protected void addAllowMethods(HttpServletResponse httpResponse, String[] httpMethods) {

		if (CollectionUtils.notEmpty(httpMethods))
			httpResponse.addHeader(CORS.Access_Control_Allow_Methods, StringHandler.join(",", httpMethods));
	}

	/**
	 * Metodo che costruisce l'oggetto innettato nel {@link HttpEntity} tornato assieme alle response.
	 * 
	 * @param message
	 * @return
	 */
	private static Object buildResponse(String message) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(RESPONSE_MESSAGE_KEY, message);
		return map.entrySet().iterator().next();
	}

	private static Object buildResponse(Object o) {
		if (o instanceof String)
			return buildResponse((String) o);
		if (o instanceof StructResp)
			return buildResponse((StructResp) o);
		if (o instanceof Exception)
			return buildResponse((Exception) o);
		return o;
	}

	private static Object buildResponse(StructResp res) {
		return res.map;
	}

	private static Object buildResponse(Exception e) {
		return collect("error", e.getMessage(), "cause", e.getCause().getMessage());
	}

	/**
	 * In questa classe vengono incapsulate in una {@link Map} le info della risposta, secondo l'ordine degli oggetti
	 * nell'array: chiave, valore, chiave, valore, ecc..
	 * 
	 * @author svaponi
	 * @created Jul 14, 2016 2:12:01 AM
	 */
	protected static class StructResp {
		Map<Object, Object> map;

		private StructResp(Object[] args) {
			map = new HashMap<Object, Object>();
			if (args != null)
				for (int i = 1; i < args.length; i += 2)
					map.put(args[i - 1], args[i]);
		}
	}

	protected static StructResp collect(Object... args) {
		return new StructResp(args);
	}

	/*
	 * HTTP CODE 200 - 299
	 * ===============================================================================================
	 */

	/**
	 * Metodo comune ausiliario, ritorna l'oggetto response assieme HTTP code 200
	 * 
	 * @param response
	 * @return
	 */
	protected static HttpEntity<?> ok() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	protected static <T> HttpEntity<?> ok(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.OK);
	}

	protected static HttpEntity<?> ok(String messageTmpl, Object... messageArgs) {
		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return ok(String.format(messageTmpl, messageArgs));
		else
			return ok(messageArgs);
	}

	/**
	 * Metodo comune ausiliario, ritorna l'oggetto response assieme HTTP code 201
	 * 
	 * @param response
	 * @return
	 */
	protected HttpEntity<?> created() {
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	protected static <T> HttpEntity<?> created(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.CREATED);
	}

	protected HttpEntity<?> created(String messageTmpl, Object... messageArgs) {

		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return created(String.format(messageTmpl, messageArgs));
		else
			return created(messageTmpl);
	}

	/**
	 * Metodo comune ausiliario, ritorna l'oggetto response assieme HTTP code 202
	 * 
	 * @param response
	 * @return
	 */
	protected HttpEntity<?> accepted() {
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	protected static <T> HttpEntity<?> accepted(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.ACCEPTED);
	}

	protected HttpEntity<?> accepted(String messageTmpl, Object... messageArgs) {
		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return accepted(String.format(messageTmpl, messageArgs));
		else
			return accepted(messageTmpl);
	}

	/**
	 * Metodo comune ausiliario, poichè non c'è contenuto non ritorna nulla bensì solo HTTP code 204
	 * 
	 * @param response
	 * @return
	 */
	protected HttpEntity<?> noContent() {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	protected static <T> HttpEntity<?> noContent(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.NO_CONTENT);
	}

	protected HttpEntity<?> noContent(String messageTmpl, Object... messageArgs) {

		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return noContent(String.format(messageTmpl, messageArgs));
		else
			return noContent(messageTmpl);
	}

	/**
	 * Metodo comune ausiliario, ritorna HTTP code 205
	 * 
	 * @param response
	 * @return
	 */
	protected HttpEntity<?> resetContent() {
		return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
	}

	protected static <T> HttpEntity<?> resetContent(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.RESET_CONTENT);
	}

	protected HttpEntity<?> resetContent(String messageTmpl, Object... messageArgs) {

		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return resetContent(String.format(messageTmpl, messageArgs));
		else
			return resetContent(messageTmpl);
	}

	/*
	 * HTTP CODE 300 - 399
	 * ===============================================================================================
	 */

	/*
	 * HTTP CODE 400 - 499
	 * ===============================================================================================
	 */

	/**
	 * Metodo comune ausiliario, ritorna HTTP code 400
	 * 
	 * @return
	 */
	protected HttpEntity<?> badRequest() {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	protected static <T> HttpEntity<?> badRequest(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.BAD_REQUEST);
	}

	protected HttpEntity<?> badRequest(String messageTmpl, Object... messageArgs) {
		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return badRequest(String.format(messageTmpl, messageArgs));
		else
			return badRequest(messageTmpl);
	}

	/**
	 * Metodo comune ausiliario, ritorna HTTP code 401
	 * 
	 * @return
	 */
	protected HttpEntity<?> unauthorized() {
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	protected static <T> HttpEntity<?> unauthorized(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.UNAUTHORIZED);
	}

	protected HttpEntity<?> unauthorized(String messageTmpl, Object... messageArgs) {
		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return unauthorized(String.format(messageTmpl, messageArgs));
		else
			return unauthorized(messageTmpl);
	}

	/**
	 * Metodo comune ausiliario, ritorna HTTP code 404
	 * 
	 * @return
	 */
	protected HttpEntity<?> notFound() {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	protected static <T> HttpEntity<?> notFound(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.NOT_FOUND);
	}

	protected HttpEntity<?> notFound(String messageTmpl, Object... messageArgs) {
		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return notFound(String.format(messageTmpl, messageArgs));
		else
			return notFound(messageTmpl);
	}

	/*
	 * HTTP CODE 500 - 599
	 * ===============================================================================================
	 */

	/**
	 * Metodo comune ausiliario, ritorna HTTP code 500
	 * 
	 * @return
	 */
	protected HttpEntity<?> serverError() {
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected static <T> HttpEntity<?> serverError(T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected HttpEntity<?> serverError(String messageTmpl, Object... messageArgs) {
		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return serverError(String.format(messageTmpl, messageArgs));
		else
			return serverError(messageTmpl);
	}

	/*
	 * HTTP CODE on parameter
	 * ===============================================================================================
	 */

	/**
	 * Metodo comune ausiliario, ritorna HTTP code 500
	 * 
	 * @return
	 */
	protected HttpEntity<?> respond(int code) {
		return new ResponseEntity<>(HttpStatus.valueOf(code));
	}

	protected static <T> HttpEntity<?> respond(int code, T response) {
		return new ResponseEntity<>(buildResponse(response), HttpStatus.valueOf(code));
	}

	protected HttpEntity<?> respond(int code, String messageTmpl, Object... messageArgs) {
		if (StringHandler.hasText(messageTmpl) && messageArgs != null && messageArgs.length > 0)
			return respond(code, String.format(messageTmpl, messageArgs));
		else
			return respond(code, messageTmpl);
	}
}
