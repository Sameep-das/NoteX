import { apiCall } from "./api";

export const notificationService = {
  async getNotifications(page = 0, size = 20) {
    return apiCall(`/notifications?page=${page}&size=${size}`);
  },

  async getUnreadNotifications() {
    return apiCall('/notifications/unread');
  },

  async getUnreadCount() {
    return apiCall('/notifications/unread-count');
  },

  async markAsRead(notificationId: number) {
    return apiCall(`/notifications/${notificationId}/read`, {
      method: 'PUT',
    });
  },

  async markAllAsRead() {
    return apiCall('/notifications/mark-all-read', {
      method: 'PUT',
    });
  },

  async deleteNotification(notificationId: number) {
    return apiCall(`/notifications/${notificationId}`, {
      method: 'DELETE',
    });
  },
};