import { apiCall } from "./api";

export const friendshipService = {
  async sendFriendRequest(friendId: number) {
    return apiCall('/friends/request', {
      method: 'POST',
      body: JSON.stringify({ friendId }),
    });
  },

  async acceptFriendRequest(requestId: number) {
    return apiCall(`/friends/${requestId}/accept`, {
      method: 'PUT',
    });
  },

  async rejectFriendRequest(requestId: number) {
    return apiCall(`/friends/${requestId}/reject`, {
      method: 'DELETE',
    });
  },

  async removeFriend(friendId: number) {
    return apiCall(`/friends/${friendId}`, {
      method: 'DELETE',
    });
  },

  async getFriends() {
    return apiCall('/friends');
  },

  async getPendingRequests() {
    return apiCall('/friends/pending');
  },

  async getOnlineFriends() {
    return apiCall('/friends/online');
  },
};