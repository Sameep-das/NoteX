import { apiCall } from "./api";

export const chatService = {
  async getGroupMessages(groupId: number, page = 0, size = 50) {
    return apiCall(`/chat/groups/${groupId}/messages?page=${page}&size=${size}`);
  },

  async getRecentMessages(groupId: number) {
    return apiCall(`/chat/groups/${groupId}/recent`);
  },

  async markAsRead(messageId: number) {
    return apiCall(`/chat/messages/${messageId}/read`, {
      method: 'PUT',
    });
  },

  async getUnreadCount(groupId: number) {
    return apiCall(`/chat/groups/${groupId}/unread-count`);
  }
}