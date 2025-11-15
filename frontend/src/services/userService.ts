import { apiCall } from "./api";

export const userService = {
  async getUserById(userId: number) {
    return apiCall(`/users/${userId}`);
  },

  async getUserByUsername(username: string) {
    return apiCall(`/users/username/${username}`);
  },

  async updateProfile(data: {
    fullName?: string;
    bio?: string;
    isPublic?: boolean;
  }) {
    return apiCall('/users/profile', {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  async updateProfilePicture(file: File) {
    const formData = new FormData();
    formData.append('file', file);

    return apiCall('/users/profile-picture', {
      method: 'POST',
      body: formData,
    });
  },

  async getOnlineUsers() {
    return apiCall('/users/online');
  },
};