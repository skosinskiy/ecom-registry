import jwt from 'jsonwebtoken'
import api from '../components/Axios/Axios'

export const getJwtToken = (requestTimeout) => {
  const { jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireTime } = getLocalStorageTokens()
  if (!jwtAccessToken) {
    return null
  }
  if (isTokenUpdateRequired(jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireTime, requestTimeout)) {
    return refreshAccessToken(jwtRefreshToken)
  }
  return jwtAccessToken
}

export const setLocalStorageTokens = jwtAccessTokens => {
  const { jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireDate } = jwtAccessTokens
  window.localStorage.setItem('jwt_access_token', jwtAccessToken)
  window.localStorage.setItem('jwt_refresh_token', jwtRefreshToken)
  window.localStorage.setItem('jwt_refresh_token_expire', jwtRefreshTokenExpireDate)
}

const getLocalStorageTokens = () => {
  return {
    'jwtAccessToken': window.localStorage.getItem('jwt_access_token'),
    'jwtRefreshToken': window.localStorage.getItem('jwt_refresh_token'),
    'jwtRefreshTokenExpireTime': window.localStorage.getItem('jwt_refresh_token_expire')
  }
}

const isTokenUpdateRequired = (jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireTime, requestTimeout) => {
  const accessTokenExpireTime = jwt.decode(jwtAccessToken).exp * 1000
  const currentTime = new Date().getTime()
  return currentTime > accessTokenExpireTime - requestTimeout &&
      jwtRefreshTokenExpireTime - requestTimeout > currentTime &&
      jwtRefreshToken
}

const refreshAccessToken = jwtRefreshToken => {
  return api.sendRequest('api/auth/refresh', {
    method: 'POST',
    data: {
      jwtRefreshToken: jwtRefreshToken
    }
  }).then(res => {
    setLocalStorageTokens(res)
    return res.jwtAccessToken
  }).catch(() => {
    window.localStorage.clear()
    return null
  })
}
