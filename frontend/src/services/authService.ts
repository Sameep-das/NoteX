import { apiCall } from "./api";
export const authService = {
  async register(data: {
    username: string;
    email: string;
    password: string;
    fullName?: string;
    isPublic?: boolean;
  }) {
    return apiCall('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async login(credentials: { usernameOrEmail: string; password: string }) {
    return apiCall('/auth/login', {
      method: 'POST',
      body: JSON.stringify(credentials),
    });
  },

  async getCurrentUser() {
    return apiCall('/auth/me');
  },

  async logout() {
    return apiCall('/auth/logout', { method: 'POST' });
  },
};