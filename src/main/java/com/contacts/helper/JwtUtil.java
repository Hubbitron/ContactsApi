package com.contacts.helper;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtUtil
{
	@Value("${jwt.auth.secret_key}")
	private String secretKey;

	@Value("${jwt.expiration.interval.minutes}")
	private long jwtExpirationIntervalMinutes;

    public String extractUsername(String token) throws MalformedJwtException, ExpiredJwtException
    {
    	Claims claims = extractAllClaims(token);
    	if (claims == null)
    		return null;
    	
    	String subject = claims.getSubject();
    	return subject;
    }
    
    public Date extractExpiration(String token) throws MalformedJwtException, ExpiredJwtException
    {
    	Claims claims = extractAllClaims(token);
    	if (claims == null)
    	{
    		Calendar cal = Calendar.getInstance();
    		cal.set(Calendar.YEAR, 1980);
    		cal.set(Calendar.MONTH, Calendar.JANUARY);
    		cal.set(Calendar.DAY_OF_MONTH, 1);

    		Date dateRepresentation = cal.getTime();
    		
    		return dateRepresentation;
    	}

    	Date expirationDate = claims.getExpiration();

    	return expirationDate;
    }
    
    private Claims extractAllClaims(String token) throws MalformedJwtException, ExpiredJwtException
    {
		Claims claims = null; 

		try
		{
			claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		}
		catch (SignatureException e)
		{
			System.out.println("signature exception" + e);
		}
		catch (MalformedJwtException e)
		{
			System.out.println("token malformed" + e);
		}
		catch (ExpiredJwtException e)
		{
			System.out.println("token expired" + e);
		}
		catch (UnsupportedJwtException e)
		{
			System.out.println("unsupported" + e);
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Illegal" + e);
		}
		
		return claims;
    }
    
    public Boolean isTokenExpired(String token) throws MalformedJwtException, ExpiredJwtException
    {
    	return extractExpiration(token).before(new Date());
    }
    
	public String generateToken(UserDetails userDetails)
	{
		Map<String, Object> claims = new HashMap<String, Object>();
		
		return this.createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(Map<String, Object> claims, String subject)
	{
		String outputString = Jwts.builder().setClaims(claims).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * this.jwtExpirationIntervalMinutes))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
		
		return outputString;
	}
	
	public Boolean validateToken(String token, UserDetails userDetails)
	{
		String username = StringUtils.EMPTY;
		boolean isExpired = true;
		
		username = extractUsername(token);
		if (username == null)
			return false;

		if (!username.equals(userDetails.getUsername()))
			return false;

		isExpired = isTokenExpired(token);
		if (isExpired)
			return false;

		return true;
		
	}
}
        