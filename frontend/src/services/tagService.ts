import { apiCall } from "./api";

export const tagService = {
  async createTag(data: { name: string; category?: string }) {
    return apiCall('/tags', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async getAllTags() {
    return apiCall('/tags');
  },

  async getTagsByCategory(category: string) {
    return apiCall(`/tags/category/${category}`);
  },

  async searchTags(keyword: string) {
    return apiCall(`/tags/search?keyword=${keyword}`);
  },

  async getTagById(tagId: number) {
    return apiCall(`/tags/${tagId}`);
  },

  async deleteTag(tagId: number) {
    return apiCall(`/tags/${tagId}`, {
      method: 'DELETE',
    });
  },
};