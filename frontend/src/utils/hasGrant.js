export const hasGrant = (user, grant) => {
  return user.permissions.includes(grant)
}