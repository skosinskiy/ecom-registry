import jwt from "jsonwebtoken";
import api from '../components/Axios/Axios'

//TODO REFACTOR!!!!!!!!!!!!!!!!!!1
export const getJwtToken = (requestParams, requestTimeout) => {
  return new Promise(resolve => {
    const {jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireTime} = getLocalStorageTokens()
    if (isTokenUpdateRequired(jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireTime, requestTimeout)) {
      return api.sendRequest('api/auth/refresh', {
        method: 'POST',
        data: {
          jwtRefreshToken: jwtRefreshToken
        }
      }).then(res => {
        setLocalStorageTokens(res.data)
        resolve(res.data.jwtAccessToken)
      }).catch(() => {
        window.localStorage.clear()
        resolve(null)
      })
    } else {
      resolve(jwtAccessToken)
    }
  })
}

const setLocalStorageTokens = jwtAccessTokens => {
  const {jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireDate} = jwtAccessTokens
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
  if (!jwtAccessToken) {
    return false;
  }
  const accessTokenExpireTime = jwt.decode(jwtAccessToken).exp * 1000
  const currentTime = new Date().getTime()
  return jwtRefreshToken &&
      currentTime > accessTokenExpireTime - requestTimeout &&
      jwtRefreshTokenExpireTime - requestTimeout > currentTime
}


