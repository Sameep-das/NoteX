// const API_BASE_URL = 'http://localhost:8080/api';
import { apiCall } from "./api";

export const groupService = {
  async createGroup(data: {
    name: string;
    description?: string;
    isPrivate?: boolean;
  }) {
    return apiCall('/groups', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async getGroupById(groupId: number) {
    return apiCall(`/groups/${groupId}`);
  },

  async updateGroup(groupId: number, data: {
    name?: string;
    description?: string;
    isPrivate?: boolean;
  }) {
    return apiCall(`/groups/${groupId}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  async deleteGroup(groupId: number) {
    return apiCall(`/groups/${groupId}`, {
      method: 'DELETE',
    });
  },

  async getUserGroups() {
    return apiCall('/groups/my-groups');
  },

  async getPublicGroups() {
    return apiCall('/groups/public');
  },

  async addMember(groupId: number, data: {
    userId: number;
    role?: 'ADMIN' | 'MODERATOR' | 'MEMBER';
  }) {
    return apiCall(`/groups/${groupId}/members`, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async removeMember(groupId: number, userId: number) {
    return apiCall(`/groups/${groupId}/members/${userId}`, {
      method: 'DELETE',
    });
  },

  async getGroupMembers(groupId: number) {
    return apiCall(`/groups/${groupId}/members`);
  },
};