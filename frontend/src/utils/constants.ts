// API Base URL
export const API_BASE_URL = 'http://localhost:8080/api';

// WebSocket URL
export const WS_BASE_URL = 'ws://localhost:8080/api/ws';

// Storage Keys
export const STORAGE_KEYS = {
  TOKEN: 'notex_token',
  USER: 'notex_user',
  THEME: 'theme',
} as const;

// API Endpoints
export const API_ENDPOINTS = {
  // Auth
  LOGIN: '/auth/login',
  REGISTER: '/auth/register',
  ME: '/auth/me',
  LOGOUT: '/auth/logout',

  // Notes
  NOTES: '/notes',
  MY_NOTES: '/notes/my-notes',
  NOTE_BY_ID: (id: number) => `/notes/${id}`,
  GROUP_NOTES: (groupId: number) => `/notes/group/${groupId}`,
  UPLOAD_NOTE_FILE: (id: number) => `/notes/${id}/upload`,

  // Groups
  GROUPS: '/groups',
  MY_GROUPS: '/groups/my-groups',
  PUBLIC_GROUPS: '/groups/public',
  GROUP_BY_ID: (id: number) => `/groups/${id}`,
  GROUP_MEMBERS: (id: number) => `/groups/${id}/members`,
  ADD_MEMBER: (id: number) => `/groups/${id}/members`,
  REMOVE_MEMBER: (id: number, userId: number) => `/groups/${id}/members/${userId}`,

  // Friends
  FRIENDS: '/friends',
  FRIEND_REQUEST: '/friends/request',
  ACCEPT_FRIEND: (id: number) => `/friends/${id}/accept`,
  REJECT_FRIEND: (id: number) => `/friends/${id}/reject`,
  REMOVE_FRIEND: (id: number) => `/friends/${id}`,
  PENDING_REQUESTS: '/friends/pending',
  ONLINE_FRIENDS: '/friends/online',

  // Chat
  GROUP_MESSAGES: (groupId: number) => `/chat/groups/${groupId}/messages`,
  RECENT_MESSAGES: (groupId: number) => `/chat/groups/${groupId}/recent`,
  MARK_READ: (messageId: number) => `/chat/messages/${messageId}/read`,
  UNREAD_COUNT: (groupId: number) => `/chat/groups/${groupId}/unread-count`,

  // Tags
  TAGS: '/tags',
  TAG_BY_ID: (id: number) => `/tags/${id}`,
  TAGS_BY_CATEGORY: (category: string) => `/tags/category/${category}`,
  SEARCH_TAGS: '/tags/search',

  // Users
  USERS: '/users',
  USER_BY_ID: (id: number) => `/users/${id}`,
  USER_BY_USERNAME: (username: string) => `/users/username/${username}`,
  UPDATE_PROFILE: '/users/profile',
  UPDATE_PROFILE_PICTURE: '/users/profile-picture',
  ONLINE_USERS: '/users/online',

  // Notifications
  NOTIFICATIONS: '/notifications',
  UNREAD_NOTIFICATIONS: '/notifications/unread',
  //changed by me as collision with previous UNREAD_COUNT
  UNREAD_NOTIFICATION_COUNT: '/notifications/unread-count',
  MARK_NOTIFICATION_READ: (id: number) => `/notifications/${id}/read`,
  MARK_ALL_READ: '/notifications/mark-all-read',
  DELETE_NOTIFICATION: (id: number) => `/notifications/${id}`,
} as const;